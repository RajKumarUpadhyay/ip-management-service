package go.ip.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "IP_POOL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPPool {
    @Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    private String description;
    private int totalCapacity;
    private int usedCapacity;
    private String lowerBound;
    private String uppperBound;
}
