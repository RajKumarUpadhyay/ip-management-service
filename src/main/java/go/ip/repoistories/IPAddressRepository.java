package go.ip.repoistories;

import go.ip.entities.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPAddressRepository extends JpaRepository<IPAddress, Integer> {

    Optional<List<IPAddress>> findAllByipPoolIdOrderByValueDesc(int ipPoolId);
    Optional<IPAddress> findByValue(String ipaddress);
}
