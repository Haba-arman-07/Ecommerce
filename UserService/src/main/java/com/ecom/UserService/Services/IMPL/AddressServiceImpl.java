package com.ecom.UserService.Services.IMPL;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressDto;
import com.ecom.CommonEntity.dtos.AddressResponseDto;
import com.ecom.CommonEntity.entities.*;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.ServiceInterface.AddressService;
import com.ecom.commonRepo.dao.AddressDao;
import com.ecom.commonRepo.dao.MasterDao;
import com.ecom.commonRepo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MasterDao masterDao;

    @Override
    public ResponseModel addAddress(AddressDto addressDto) {
        try {
            Users existUser = userDao.findUserByIdAndStatus(addressDto.getUserId(), Status.ACTIVE)
                    .orElseThrow(() -> new NoSuchElementException("User Not Exist"));

            Country existCountry = masterDao.getCountryRepo().findById(addressDto.getCountryId())
                    .orElseThrow(() -> new IllegalArgumentException("Country Not Exist"));

            State existState = masterDao.getStateRepo().findById(addressDto.getStateId())
                    .orElseThrow(() -> new IllegalArgumentException("State Not Exist"));

            City existCity = masterDao.getCityRepo().findById(addressDto.getCityId())
                    .orElseThrow(() -> new IllegalArgumentException("City Not Exist"));


            if (!existState.getCountry().getCountryId().equals(existCountry.getCountryId())) {
                throw new IllegalArgumentException("State Does't Belong To Specify Country");
            }

            if (!existCity.getState().getStateId().equals(existState.getStateId())) {
                throw new IllegalArgumentException("City Does't Belong To Specify State");
            }

            Address address = AddressDto.toEntity(addressDto, existUser, existCountry,
                    existState, existCity);
            Address saved = addressDao.saveAddress(address);

            return new ResponseModel(
                    HttpStatus.OK,
                    AddressDto.toDto(saved),
                    "Address Add Successfully"
            );

        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel getAddress(Long userId) {
        try {
            List<AddressResponseDto> address = addressDao.findUserByIdAndStatus(userId,Status.ACTIVE)
                    .stream()
                    .map(AddressResponseDto::response)
                    .toList();

            if (address.isEmpty())

                return new ResponseModel(
                        HttpStatus.NOT_FOUND,
                        null,
                        "User Not Found"
                );

            return new ResponseModel(
                    HttpStatus.OK,
                    address,
                    "SUCCESS"
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    "Get Address Error"
            );
        }
    }


    @Override
    public ResponseModel getAllAddress() {
        try {

            List<AddressResponseDto> existUsers = addressDao.findAllAddress().stream()
                    .map(AddressResponseDto::response)
                    .toList();

            return new ResponseModel(
                    HttpStatus.OK,
                    existUsers,
                    "SUCCESS"
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    "Get All Address Failed"
            );
        }
    }

    @Override
    public ResponseModel updateAddress(AddressDto addressDto) {
        try {

            Address address = addressDao.findById(addressDto.getAddressId())
                    .orElseThrow(() -> new NoSuchElementException("Address Id Not Found"));

            Country country = masterDao.getCountryRepo().findById(addressDto.getCountryId())
                    .orElseThrow(() -> new IllegalArgumentException("Country Id Not Found"));

            State state = masterDao.getStateRepo().findById(addressDto.getStateId())
                    .orElseThrow(() -> new IllegalArgumentException("State Id Not Found"));

            City city = masterDao.getCityRepo().findById(addressDto.getCityId())
                    .orElseThrow(() -> new IllegalArgumentException("City Id Not Found"));

            if (!state.getCountry().getCountryId().equals(country.getCountryId())) {
                throw new IllegalArgumentException("State Does't Belong To Specify Country");
            }

            if (!city.getState().getStateId().equals(state.getStateId())) {
                throw new IllegalArgumentException("City Does't Belong To Specify State");
            }

            AddressDto.updateAddress(addressDto, address, country, state, city);

            Address saveAddress = addressDao.saveAddress(address);

            return new ResponseModel(
                    HttpStatus.OK,
                    AddressResponseDto.response(saveAddress),
                    "Address Update Successfully"
            );

        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel deleteAddress(Long addressId) {
        try {
            Address address = addressDao.findById(addressId)
                    .orElseThrow(() -> new NoSuchElementException("Address Id Not Exist"));

            addressDao.deleteAddressById(address.getAddressId());

            return new ResponseModel(
                    HttpStatus.OK,
                    null,
                    "Address Deleted Successfully"
            );

        } catch (NoSuchElementException e) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    e.getMessage()
            );
        }
    }


    // Get All Country
    @Override
    public ResponseModel getAllCountry() {
        try {
            List<Country> getCountry = masterDao.getCountryRepo().findAll();

            return new ResponseModel(
                    HttpStatus.OK,
                    getCountry,
                    "SUCCESS"
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Error To Fetch Country"
            );
        }
    }

    // Get All State
    @Override
    public ResponseModel getAllState() {
        try {

            List<State> getState = masterDao.getStateRepo().findAll();

            return new ResponseModel(
                    HttpStatus.OK,
                    getState,
                    "SUCCESS"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Error To Fetch State"
            );
        }
    }

    // Get All City
    @Override
    public ResponseModel getAllCity() {
        try {

            List<City> getCity = masterDao.getCityRepo().findAll();

            return new ResponseModel(
                    HttpStatus.OK,
                    getCity,
                    "SUCCESS"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Error To Fetch City"
            );
        }
    }
}


