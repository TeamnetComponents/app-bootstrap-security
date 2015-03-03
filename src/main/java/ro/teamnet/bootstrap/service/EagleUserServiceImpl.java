package ro.teamnet.bootstrap.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Service class for managing EagleUsers.
 */
@Service
@Transactional
public class EagleUserServiceImpl implements EagleUserService {

    private final Logger log = LoggerFactory.getLogger(EagleUserServiceImpl.class);

}
