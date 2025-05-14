package com.pqkhang.ct553_backend.domain.user.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.user.dto.AddressDTO;
import com.pqkhang.ct553_backend.domain.user.entity.Address;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import com.pqkhang.ct553_backend.domain.user.mapper.AddressMapper;
import com.pqkhang.ct553_backend.domain.user.repository.AddressRepository;
import com.pqkhang.ct553_backend.domain.user.repository.CustomerRepository;
import com.pqkhang.ct553_backend.domain.user.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AddressServiceImpl implements AddressService {

    AddressRepository addressRepository;
    AddressMapper addressMapper;
    private final CustomerRepository customerRepository;

    @Override
    public List<AddressDTO> getAllAddressesByCustomerId(UUID customerId) throws ResourceNotFoundException {
        List<Address> addresses = addressRepository.findAllByCustomer_CustomerId(customerId);
        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("No address found");
        }
        return addresses.stream()
                .map(addressMapper::toAddressDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getDefaultAddressByCustomerId(UUID customerId) throws ResourceNotFoundException {
        Address address = addressRepository.findAllByCustomer_CustomerId(customerId).stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No default address found"));
        return addressMapper.toAddressDTO(address);
    }

    @Override
    public void setDefaultAddress(UUID customerId, UUID addressId) throws ResourceNotFoundException {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Address newDefaultAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!newDefaultAddress.getCustomer().getCustomerId().equals(customerId)) {
            throw new ResourceNotFoundException("Address does not belong to customer");
        }

        Address currentDefaultAddress = addressRepository.findByCustomer_CustomerIdAndIsDefault(customerId, true);

        if (currentDefaultAddress != null) {
            currentDefaultAddress.setIsDefault(false);
            addressRepository.save(currentDefaultAddress);
        }

        newDefaultAddress.setIsDefault(true);
        addressRepository.save(newDefaultAddress);
    }

    @Override
    public AddressDTO createAddress(UUID customerId, AddressDTO addressDTO) throws ResourceNotFoundException {
        Address address = addressRepository.findAddressesByCustomer_CustomerIdAndProvinceIdAndDistrictIdAndWardCodeAndDescription(
                customerId,
                addressDTO.getProvinceId(),
                addressDTO.getDistrictId(),
                addressDTO.getWardCode(),
                addressDTO.getDescription()
        );
        if (address != null) {
            throw new ResourceNotFoundException("Address already exists");
        }

        Address currentDefaultAddress = addressRepository.findByCustomer_CustomerIdAndIsDefault(customerId, true);
        if (currentDefaultAddress != null) {
            currentDefaultAddress.setIsDefault(false);
            addressRepository.save(currentDefaultAddress);
        }

        address = addressMapper.toAddress(addressDTO);
        address.setCustomer(Customer.builder().customerId(customerId).build());
        return addressMapper.toAddressDTO(addressRepository.save(address));
    }

    @Override
    public AddressDTO updateAddress(UUID id, AddressDTO addressDTO) throws ResourceNotFoundException {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (addressDTO.getProvinceId().equals(address.getProvinceId())
                && addressDTO.getDistrictId().equals(address.getDistrictId())
                && addressDTO.getWardCode().equals(address.getWardCode())
                && addressDTO.getDescription().equals(address.getDescription())
                && addressDTO.getIsDefault().equals(address.getIsDefault())) {
            throw new ResourceNotFoundException("No change detected");
        }
        address.setProvinceId(addressDTO.getProvinceId());
        address.setDistrictId(addressDTO.getDistrictId());
        address.setWardCode(addressDTO.getWardCode());
        address.setDescription(addressDTO.getDescription());
        address.setIsDefault(addressDTO.getIsDefault());
        return addressMapper.toAddressDTO(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(UUID id) throws ResourceNotFoundException {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        addressRepository.delete(address);
    }

}

