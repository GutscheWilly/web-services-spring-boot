package com.willy.course.services;

import com.willy.course.entities.User;
import com.willy.course.repositories.UserRepository;
import com.willy.course.services.exceptions.DatabaseException;
import com.willy.course.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public User insert(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        try {
            userRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    public User update(Long id, User updatedUser) {
        try {
            User currentUser = userRepository.getReferenceById(id);
            updateData(currentUser, updatedUser);
            return userRepository.save(currentUser);
        }
        catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(User currentUser, User updatedUser) {
        currentUser.setName(updatedUser.getName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setPhone(updatedUser.getPhone());
    }
}
