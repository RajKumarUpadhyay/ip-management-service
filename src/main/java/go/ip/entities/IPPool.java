package go.ip.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "IP_POOL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IPPool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(name = "DESCRIPTION", nullable = false, unique = true)
    private String description;
    @Column(name = "TOTAL_CAPACITY", nullable = false, precision = 0)
    private int totalCapacity;
    @Column(name = "USED_CAPACITY", nullable = false, precision = 0)
    private int usedCapacity;
    @Column(name = "LOWER_BOUND", nullable = false)
    private String lowerBound;
    @Column(name = "UPPER_BOUND", nullable = false)
    private String upperBound;
}
