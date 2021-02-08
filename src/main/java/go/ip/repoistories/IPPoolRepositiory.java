package go.ip.repoistories;

import go.ip.entities.IPPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPPoolRepositiory extends JpaRepository<IPPool, Integer> {
}
