package go.ip;

import go.ip.entities.IPPool;
import go.ip.repoistories.IPPoolRepositiory;
import go.ip.utils.IPManagementServiceUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class IpManagementServiceApplication implements CommandLineRunner {

	public static final Logger logger = Logger.getLogger(IpManagementServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IpManagementServiceApplication.class, args);
	}

	@Autowired
	IPManagementServiceUtils ipManagementServiceUtils;

	@Autowired
	IPPoolRepositiory ipPoolRepositiory;

	@Override
	public void run(String... args) throws Exception {

		List<IPPool> ipPoolList = new ArrayList<>();

		IPPool ipPool1 = new IPPool();
		ipPool1.setDescription("Static - IP Block 1");
		ipPool1.setLowerBound("10.70.26.1");
		ipPool1.setUpperBound("10.70.26.100");
		ipPool1.setTotalCapacity(ipManagementServiceUtils.calculateCapacity(ipPool1.getLowerBound(), ipPool1.getUpperBound()));
		ipPool1.setUsedCapacity(0);
		ipPoolRepositiory.save(ipPool1);
		logger.info("Added first pool entry : " + ipPool1.toString());

		IPPool ipPool2 = new IPPool();
		ipPool2.setDescription("Static - IP Block 2");
		ipPool2.setLowerBound("10.70.25.0");
		ipPool2.setUpperBound("10.70.25.255");
		ipPool2.setTotalCapacity(ipManagementServiceUtils.calculateCapacity(ipPool2.getLowerBound(), ipPool2.getUpperBound()));
		ipPool2.setUsedCapacity(0);
		ipPoolRepositiory.save(ipPool2);
		logger.info("Added second pool entry : " + ipPool2.toString());

		IPPool ipPool3 = new IPPool();
		ipPool3.setDescription("Dynamic - IP Block 3");
		ipPool3.setLowerBound("10.50.0.0");
		ipPool3.setUpperBound("10.50.255.255");
		ipPool3.setTotalCapacity(ipManagementServiceUtils.calculateCapacity(ipPool3.getLowerBound(), ipPool3.getUpperBound()));
		ipPool3.setUsedCapacity(0);
		ipPoolRepositiory.save(ipPool3);
		logger.info("Added third pool entry : " + ipPool1.toString());
	}
}
