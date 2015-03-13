package ro.teamnet.bootstrap.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing LocalCenters.
 */
@Service
@Transactional
public class LocalCenterServiceImpl implements LocalCenterService {

    private final Logger log = LoggerFactory.getLogger(LocalCenterServiceImpl.class);
}
