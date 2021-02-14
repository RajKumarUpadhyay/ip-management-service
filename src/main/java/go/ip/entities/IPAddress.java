package go.ip.entities;

import go.ip.common.ResourceState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "IP_Address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    @Column(name = "IP_POOL_ID")
    private int ipPoolId;
    @Column(name = "IP_ADDRESS", unique = true)
    private String value;
    @Column(name = "STATE", nullable = false)
    private String resourceState;
}