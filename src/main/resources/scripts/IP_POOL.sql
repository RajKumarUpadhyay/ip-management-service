DROP TABLE  IF EXISTS IP_POOL;
CREATE TABLE IP_POOL (
   id INT NOT NULL,
   description VARCHAR(50) NOT NULL,
   totalCapacity INT NOT NULL,
   usedCapacity INT NOT NULL,
  lowerBound VARCHAR(50) NOT NULL,
  uppperBound VARCHAR(50) NOT NULL
);