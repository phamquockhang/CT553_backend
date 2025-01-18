package com.pqkhang.ct553_backend.domain.auth.object.customer.address;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.auth.object.customer.Customer;
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

