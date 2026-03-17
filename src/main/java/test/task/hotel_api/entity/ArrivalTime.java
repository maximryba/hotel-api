package test.task.hotel_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arrival_times")
public class ArrivalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arrival_times_seq")
    @SequenceGenerator(name = "arrival_times_seq", sequenceName = "arrival_times_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private LocalTime checkOut;

    @OneToOne(mappedBy = "arrivalTime")
    private Hotel hotel;

}
