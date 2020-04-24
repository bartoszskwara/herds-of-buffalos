package com.buffalosoftware.generate;

import com.buffalosoftware.api.generator.IGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generate")
@RequiredArgsConstructor
public class GeneratorController {

    private final IGeneratorService generatorService;

    @GetMapping("")
    public ResponseEntity getAllAvailableBuildings() {
        generatorService.generate();
        return ResponseEntity.ok().build();
    }
}
