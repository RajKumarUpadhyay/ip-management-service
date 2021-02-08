package go.ip.entities;

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
    private int ipPoolId;
    private String value;
    private Enum resourceState;
}
