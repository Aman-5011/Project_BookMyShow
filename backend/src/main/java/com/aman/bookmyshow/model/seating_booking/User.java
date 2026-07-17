package com.aman.bookmyshow.model.seating_booking;

import com.aman.bookmyshow.model.venues.Screen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private int id;
    private String name;
    private String email;
    private String password;
}
