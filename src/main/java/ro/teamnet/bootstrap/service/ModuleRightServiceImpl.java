package ro.teamnet.bootstrap.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;
import ro.teamnet.bootstrap.repository.ModuleRightRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


/**
 * Service class for managing  ModuleRights.
 */
@Service
@Transactional(value="transactionManager", readOnly = true)
public class ModuleRightServiceImpl extends AbstractServiceImpl<ModuleRight,Long> implements ModuleRightService {

    private final Logger log = LoggerFactory.getLogger(ModuleRightServiceImpl.class);


    private final ModuleRightRepository moduleRightRepository;

    @Inject
    public ModuleRightServiceImpl(ModuleRightRepository repository) {
        super(repository);
        this.moduleRightRepository=repository;
    }


    @Override
    public List getModuleRightCodes() {
        class Pair{
            short right;
            String moduleRightCode;

            public short getRight() {
                return right;
            }

            public void setRight(short right) {
                this.right = right;
            }

            public String getModuleRightCode() {
                return moduleRightCode;
            }

            public void setModuleRightCode(String moduleRightCode) {
                this.moduleRightCode = moduleRightCode;
            }
        }

        List moduleRightCodes = new ArrayList();
        for(ModuleRightTypeEnum moduleRightTypeEnum : ModuleRightTypeEnum.values()){
            Pair pair = new Pair();

            pair.setRight(moduleRightTypeEnum.getRight());
            pair.setModuleRightCode(ModuleRightTypeEnum.getCodeByValue(moduleRightTypeEnum.getRight()));

            moduleRightCodes.add(pair);
        }

        return moduleRightCodes;
    }

    @Override
    public List<ModuleRight> findByModuleAndRight(Module module, Short right) {
        return moduleRightRepository.findByModuleAndRight(module, right);
    }

    @Override
    public List<ModuleRight> findAll() {
        return new ArrayList<>(moduleRightRepository.findAllWithModule());
    }
}
