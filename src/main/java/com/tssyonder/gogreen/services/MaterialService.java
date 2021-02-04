package com.tssyonder.gogreen.services;

import com.tssyonder.gogreen.entities.Material;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaterialService {

    List<Material> getAllMaterials();

    Material getMaterialById(Long materialId);

    Material getMaterialByName(String materialName);

    Material saveMaterial(Material material);

    List<Material> saveAllMaterial(List<Material> materialList);
}
