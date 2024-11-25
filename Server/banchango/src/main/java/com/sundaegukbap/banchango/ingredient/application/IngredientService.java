package com.sundaegukbap.banchango.ingredient.application;

import com.sundaegukbap.banchango.container.domain.Container;
import com.sundaegukbap.banchango.container.repository.ContainerRepository;
import com.sundaegukbap.banchango.ingredient.domain.ContainerIngredient;
import com.sundaegukbap.banchango.ingredient.domain.Ingredient;
import com.sundaegukbap.banchango.ingredient.dto.IngredientInsertRequest;
import com.sundaegukbap.banchango.ingredient.dto.event.IngredientChangedEvent;
import com.sundaegukbap.banchango.ingredient.repository.ContainerIngredientRepository;
import com.sundaegukbap.banchango.ingredient.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IngredientService {

    private final ContainerIngredientRepository containerIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final ContainerRepository containerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void insertIngredient(Long userId, IngredientInsertRequest request) {
        Container container = containerRepository.findById(request.containerId())
            .orElseThrow(() -> new NoSuchElementException("no container"));
        Ingredient ingredient = ingredientRepository.findById(request.ingredientId())
            .orElseThrow(() -> new NoSuchElementException("no ingredient"));

        ContainerIngredient containerIngredient = request.toEntity(container, ingredient);
        containerIngredientRepository.save(containerIngredient);

        applicationEventPublisher.publishEvent(new IngredientChangedEvent(userId));
    }

    @Transactional
    public void removeIngredient(Long userId, Long containerIngredientId) {
        ContainerIngredient containerIngredient = containerIngredientRepository.findById(
                containerIngredientId)
            .orElseThrow(() -> new NoSuchElementException("no ingredient in container"));

        containerIngredientRepository.delete(containerIngredient);

        applicationEventPublisher.publishEvent(new IngredientChangedEvent(userId));
    }
}
