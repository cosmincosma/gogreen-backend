package com.tssyonder.gogreen.services.impl;

import com.tssyonder.gogreen.entities.Material;
import com.tssyonder.gogreen.repositories.MaterialRepository;
import com.tssyonder.gogreen.services.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public Material getMaterialById(Long materialId) {
        return materialRepository.findByMaterialId(materialId);
    }

    @Override
    public Material getMaterialByName(String materialName) {
        return materialRepository.findByMaterialName(materialName);
    }

    @Override
    public Material saveMaterial(Material material) {
        return materialRepository.saveAndFlush(material);
    }

    @Override
    public List<Material> saveAllMaterial(List<Material> materialList) {
        return materialRepository.saveAll(materialList);
    }
}
