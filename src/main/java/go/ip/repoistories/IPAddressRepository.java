package go.ip.repoistories;

import go.ip.entities.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPAddressRepository extends JpaRepository<IPAddress, Integer> {
}
