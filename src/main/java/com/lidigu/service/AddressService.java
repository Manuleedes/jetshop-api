package com.lidigu.service;

import com.lidigu.entity.UserAddress;
import com.lidigu.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private UserAddressRepository addressRepository;

    @Transactional
    public Map<String, Object> addAddress(Long user_id, String address, String city, String state,
            String zip_code, String country, Double latitude,
            Double longitude, String type, Integer default_address,
            String google_address) {
        Map<String, Object> response = new HashMap<>();

        if (default_address == 1) {
            addressRepository.resetDefaultAddress(user_id);
        }

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(user_id);
        userAddress.setAddress(address);
        userAddress.setCity(city);
        userAddress.setState(state);
        userAddress.setZipCode(zip_code);
        userAddress.setCountry(country);
        userAddress.setLatitude(latitude);
        userAddress.setLongitude(longitude);
        userAddress.setType(type);
        userAddress.setDefaultAddress(default_address);
        userAddress.setGoogleAddress(google_address);

        addressRepository.save(userAddress);
        response.put("status", "success");
        response.put("flag", "address_added");
        response.put("message", "Address added successfully.");
        return response;
    }

    public Map<String, Object> listAddresses(Long user_id) {
        Map<String, Object> response = new HashMap<>();
        List<UserAddress> addresses = addressRepository.findByUserId(user_id);
        response.put("status", "success");
        response.put("flag", "addresses_retrieved");
        response.put("data", addresses);
        return response;
    }

    public Map<String, Object> deleteAddress(Long address_id) {
        Map<String, Object> response = new HashMap<>();
        if (addressRepository.existsById(address_id)) {
            addressRepository.deleteById(address_id);
            response.put("status", "success");
            response.put("flag", "address_deleted");
            response.put("message", "Address deleted successfully.");
        } else {
            response.put("status", "error");
            response.put("flag", "not_found");
            response.put("message", "Address not found.");
        }
        return response;
    }

    @Transactional
    public Map<String, Object> updateAddress(Long address_id, Long user_id, String address, String city,
            String state, String zip_code, String country, Double latitude,
            Double longitude, String type, Integer default_address,
            String google_address) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserAddress> addressOpt = addressRepository.findById(address_id);

        if (addressOpt.isPresent()) {
            UserAddress userAddress = addressOpt.get();
            if (default_address == 1) {
                addressRepository.resetDefaultAddress(user_id);
            }

            userAddress.setAddress(address);
            userAddress.setCity(city);
            userAddress.setState(state);
            userAddress.setZipCode(zip_code);
            userAddress.setCountry(country);
            userAddress.setLatitude(latitude);
            userAddress.setLongitude(longitude);
            userAddress.setType(type);
            userAddress.setDefaultAddress(default_address);
            userAddress.setGoogleAddress(google_address);

            addressRepository.save(userAddress);
            response.put("status", "success");
            response.put("flag", "address_updated");
            response.put("message", "Address updated successfully.");
        } else {
            response.put("status", "error");
            response.put("flag", "not_found");
            response.put("message", "Address not found.");
        }
        return response;
    }

    @Transactional
    public Map<String, Object> setDefaultAddress(Long user_id, Long address_id) {
        Map<String, Object> response = new HashMap<>();
        try {
            addressRepository.resetDefaultAddress(user_id);
            Optional<UserAddress> addressOpt = addressRepository.findById(address_id);
            if (addressOpt.isPresent()) {
                UserAddress address = addressOpt.get();
                address.setDefaultAddress(1);
                addressRepository.save(address);
                response.put("status", "success");
                response.put("flag", "default_set");
                response.put("message", "Address default status updated successfully.");
            } else {
                response.put("status", "error");
                response.put("flag", "not_found");
                response.put("message", "Address not found.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("flag", "error");
            response.put("message", "Failed to update default address. Error: " + e.getMessage());
        }
        return response;
    }
}
