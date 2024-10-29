package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HotelManagementRepository {
    HashMap<String,Hotel> hotelHashMap = new HashMap<>();
    HashMap<Integer,User> userHashMap = new HashMap<>();
    HashMap<String, Booking> bookingHashMap = new HashMap<>();
    HashMap<Integer,List<Booking>> userBookingList = new HashMap<>();
    private String hotelWithMaxFacility = "";
    private int maxFacilitiesCount = 0;
    public String addHotel(Hotel hotel) {
        if(hotel==null){
            return "FAILURE";
        }
        String key = hotel.getHotelName();
        if(key==null){
            return "FAILURE";
        }
        if(hotelHashMap.containsKey(key)){
            return "FAILURE";
        }
        hotelHashMap.put(key,hotel);

        int countOfFacilitiesInHotel=hotel.getFacilities().size();
        if(countOfFacilitiesInHotel>=maxFacilitiesCount){
            if(countOfFacilitiesInHotel==maxFacilitiesCount){
                if(hotel.getHotelName().compareTo(hotelWithMaxFacility)<0){
                    hotelWithMaxFacility = hotel.getHotelName();
                }
            }else{
                maxFacilitiesCount = countOfFacilitiesInHotel;
                hotelWithMaxFacility = hotel.getHotelName();
            }
        }
        return "SUCCESS";

    }

    public Integer addUser(User user) {
        userHashMap.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        return hotelWithMaxFacility;
    }

    public int bookARoom(Booking booking) {
        Hotel hotelToBeBooked = hotelHashMap.get(booking.getHotelName());
        if(booking.getNoOfRooms()>hotelToBeBooked.getAvailableRooms()){
            return -1;
        }
        else{
            hotelToBeBooked.setAvailableRooms(hotelToBeBooked.getAvailableRooms()-booking.getNoOfRooms());
            booking.setBookingId(String.valueOf(UUID.randomUUID()));
            booking.setAmountToBePaid(booking.getNoOfRooms() * hotelToBeBooked.getPricePerNight());
            bookingHashMap.put(booking.getBookingId(), booking);
            if(!userBookingList.containsKey(booking.getBookingAadharCard())){
                userBookingList.put(booking.getBookingAadharCard(), new ArrayList<>());
            }
            userBookingList.get(booking.getBookingAadharCard()).add(booking);
            return booking.getAmountToBePaid();
        }
    }

    public int getBookings(Integer aadharCard) {
        return userBookingList.get(aadharCard).size();
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        if(!hotelHashMap.containsKey(hotelName)){
            return new Hotel();
        }

        Hotel currHotel = hotelHashMap.get(hotelName);

        for(Facility facility: newFacilities){
            if(!currHotel.getFacilities().contains(facility)){
                currHotel.getFacilities().add(facility);
            }
        }

        int countOfFacilities = currHotel.getFacilities().size();
        if(countOfFacilities>=maxFacilitiesCount){
            if(countOfFacilities==maxFacilitiesCount){
                if(currHotel.getHotelName().compareTo(hotelWithMaxFacility)<0){
                    hotelWithMaxFacility = currHotel.getHotelName();
                }
            }else{
                maxFacilitiesCount = countOfFacilities;
                hotelWithMaxFacility = currHotel.getHotelName();
            }
        }
        return currHotel;
    }
}