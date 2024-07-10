package com.patika.emlakburadaadservice.model;

import com.patika.emlakburadaadservice.model.enums.AdStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ad_title", nullable = false)
    private String title;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_status", nullable = false)
    private AdStatus adStatus;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "ad_code", nullable = false)
    private String adCode;

    @Column(name = "ad_details", nullable = false)
    private String adDetails;

//    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Column(name = "ad_location", nullable = false)
    private String location;

    @Column(name = "ad_price", nullable = false)
    private BigDecimal price;

    private String imageUrl;
    private String category;

}
