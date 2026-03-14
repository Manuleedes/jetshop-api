package com.lidigu.controller;

import com.lidigu.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("add_address")
    public Map<String, Object> addAddress(@RequestParam Long user_id,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String zip_code,
            @RequestParam String country,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String type,
            @RequestParam Integer default_address,
            @RequestParam(required = false) String google_address) {
        return addressService.addAddress(user_id, address, city, state, zip_code, country, latitude, longitude, type,
                default_address, google_address);
    }

    @PostMapping("list_addresses")
    public Map<String, Object> listAddresses(@RequestParam Long user_id) {
        return addressService.listAddresses(user_id);
    }

    @PostMapping("delete_address")
    public Map<String, Object> deleteAddress(@RequestParam Long address_id) {
        return addressService.deleteAddress(address_id);
    }

    @PostMapping("update_address")
    public Map<String, Object> updateAddress(@RequestParam Long address_id,
            @RequestParam Long user_id,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String zip_code,
            @RequestParam String country,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam String type,
            @RequestParam Integer default_address,
            @RequestParam(required = false) String google_address) {
        return addressService.updateAddress(address_id, user_id, address, city, state, zip_code, country, latitude,
                longitude, type, default_address, google_address);
    }

    @PostMapping("setDefaultAddress")
    public Map<String, Object> setDefaultAddress(@RequestParam Long user_id,
            @RequestParam Long address_id) {
        return addressService.setDefaultAddress(user_id, address_id);
    }
}
