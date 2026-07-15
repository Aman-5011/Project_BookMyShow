package com.aman.bookmyshow.model.venues;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Screen {
    @Id
    private int id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Theater theaterId;
}
