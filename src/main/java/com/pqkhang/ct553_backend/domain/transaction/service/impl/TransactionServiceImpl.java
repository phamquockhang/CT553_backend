package com.pqkhang.ct553_backend.domain.transaction.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.request.SearchCriteria;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OverviewSellingOrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.mapper.SellingOrderMapper;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderService;
import com.pqkhang.ct553_backend.domain.transaction.dto.TransactionDTO;
import com.pqkhang.ct553_backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.pqkhang.ct553_backend.domain.transaction.dto.response.VNPayResponse;
import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;
import com.pqkhang.ct553_backend.domain.transaction.enums.TransactionStatusEnum;
import com.pqkhang.ct553_backend.domain.transaction.mapper.TransactionMapper;
import com.pqkhang.ct553_backend.domain.transaction.repository.TransactionRepository;
import com.pqkhang.ct553_backend.domain.transaction.service.TransactionService;
import com.pqkhang.ct553_backend.infrastructure.service.PaymentServiceImpl;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.VNPayUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionServiceImpl implements TransactionService {
    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;
    PaymentServiceImpl paymentService;
    SellingOrderRepository sellingOrderRepository;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    private final SellingOrderService sellingOrderService;
    private final SellingOrderMapper sellingOrderMapper;

    private String getPaymentUrlIfNeeded(HttpServletRequest request, Transaction transaction) throws ResourceNotFoundException {
        if (transaction.getPaymentMethod() != null && "VN_Pay".equals(transaction.getPaymentMethod().getPaymentMethodName())) {
            VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request, transaction, transaction.getTxnRef());
            return vnPayResponse.getPaymentUrl();
        }
        return "";
    }

    private Specification<Transaction> getTransactionSpec(Map<String, String> params) {
        Specification<Transaction> spec = Specification.where(null);

        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) -> {
//                query.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class,
                                        criteriaBuilder.lower(
                                                root.get("sellingOrder").get("sellingOrderId"))), likePattern),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class,
                                        criteriaBuilder.lower(
                                                root.get("txnRef"))), likePattern),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class,
                                        criteriaBuilder.lower(
                                                root.get("paymentMethod").get("paymentMethodName"))), likePattern)
                );
            });
        }

        if (params.containsKey("status")) {
            List<SearchCriteria> transactionStatusCriteria = requestParamUtils.getSearchCriteria(params, "status");
            if (!transactionStatusCriteria.isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    List<Predicate> predicates = transactionStatusCriteria.stream().map(criteria -> cb.equal(root.get(criteria.getKey()), criteria.getValue())).toList();
                    return cb.or(predicates.toArray(new Predicate[0]));
                });
            }
        }

        return spec;
    }

    @Override
    public TransactionDTO getTransactionById(Integer transactionId) throws ResourceNotFoundException {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return transactionMapper.toTransactionDTO(transaction);
    }

    private TransactionDTO customTransactionDTOForGetAll(Transaction transaction) {
        TransactionDTO transactionDTO = transactionMapper.toTransactionDTO(transaction);

        OverviewSellingOrderDTO overviewSellingOrderDTO = sellingOrderMapper.toOverviewSellingOrderDTO(transaction.getSellingOrder());
        transactionDTO.setSellingOrder(sellingOrderMapper.toSellingOrderDTO(overviewSellingOrderDTO));

        return transactionDTO;
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(HttpServletRequest request, TransactionDTO transactionDTO) throws ResourceNotFoundException {
        SellingOrder sellingOrder = sellingOrderRepository.findById(transactionDTO.getSellingOrder().getSellingOrderId()).orElseThrow(() -> new ResourceNotFoundException("Selling order not found"));

        sellingOrder.setPaymentStatus(PaymentStatusEnum.PENDING);
//        sellingOrder.setOrderStatus(OrderStatusEnum.PENDING);
        sellingOrderRepository.save(sellingOrder);

        Transaction transaction = transactionMapper.toTransaction(transactionDTO);
        transaction.setStatus(TransactionStatusEnum.PENDING);
        transaction.setSellingOrder(sellingOrder);
        transaction.setAmount(sellingOrder.getTotalAmount());

        String txnRef = VNPayUtils.getRandomNumber(8);
        transaction.setTxnRef(txnRef);

        Transaction savedTransaction = transactionRepository.save(transaction);

        String paymentUrl = getPaymentUrlIfNeeded(request, savedTransaction);
        TransactionDTO savedTransactionDTO = transactionMapper.toTransactionDTO(savedTransaction);

        if (savedTransactionDTO.getPaymentMethod() != null) {
            savedTransactionDTO.getPaymentMethod().setPaymentUrl(paymentUrl);
        }
        return savedTransactionDTO;
    }

    @Override
    @Transactional
    public TransactionDTO handleVNPayCallback(VNPayCallbackRequest request) throws Exception {
        String status = request.getVnp_ResponseCode();
        String txnRef = request.getVnp_TxnRef();
        String sellingOrderId = request.getVnp_OrderInfo();

        System.out.println("status: " + status);

        Transaction transaction = transactionRepository.findByTxnRef(txnRef).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        SellingOrder sellingOrder = sellingOrderRepository.findById(sellingOrderId).orElseThrow(() -> new ResourceNotFoundException("Selling order not found"));

        switch (status) {
            case "00" -> {
                sellingOrderService.updateSellingOrderStatus(sellingOrderId, sellingOrder.getOrderStatus(), PaymentStatusEnum.SUCCESS);
                transaction.setStatus(TransactionStatusEnum.SUCCESS);
            }
            case "24" -> {
                sellingOrderService.updateSellingOrderStatus(sellingOrderId, sellingOrder.getOrderStatus(), PaymentStatusEnum.CANCELLED);
                transaction.setStatus(TransactionStatusEnum.CANCELLED);
            }
            case "15" -> {
                sellingOrderService.updateSellingOrderStatus(sellingOrderId, sellingOrder.getOrderStatus(), PaymentStatusEnum.EXPIRED);
                transaction.setStatus(TransactionStatusEnum.EXPIRED);
            }
            case null, default -> {
                sellingOrderService.updateSellingOrderStatus(sellingOrderId, sellingOrder.getOrderStatus(), PaymentStatusEnum.FAILED);
                transaction.setStatus(TransactionStatusEnum.FAILED);
            }
        }

        return transactionMapper.toTransactionDTO(transaction);
    }

    @Override
    public Page<TransactionDTO> getAllTransactions(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Transaction> spec = getTransactionSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Transaction.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        if (transactionPage.isEmpty()) {
            throw new ResourceNotFoundException("Không tìm thấy giao dịch nào");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(transactionPage.getTotalPages())
                .total(transactionPage.getTotalElements())
                .build();

        return Page.<TransactionDTO>builder()
                .meta(meta)
                .data(transactionPage.getContent().stream()
                        .map(this::customTransactionDTOForGetAll)
                        .collect(Collectors.toList())
                ).build();
    }

    @Override
    @Transactional
    public void checkAndUpdateExpiredTransactions() {
//        LocalDateTime minusTime = LocalDateTime.now().minusMinutes(15);
        LocalDateTime minusTime = LocalDateTime.now().minusSeconds(15);
        int updateExpiredTransactions = transactionRepository.updateExpiredTransactions(minusTime);
        int updateExpiredSellingOrders = sellingOrderRepository.updateExpiredSellingOrders(minusTime);

        if(updateExpiredTransactions > 0) {
            System.out.println("Updated " + updateExpiredTransactions + " expired transactions");
        }

        if(updateExpiredSellingOrders > 0) {
            System.out.println("Updated " + updateExpiredSellingOrders + " expired selling orders");
        }
    }
}