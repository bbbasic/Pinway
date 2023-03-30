package com.example.notificationservice.services;

import com.example.notificationservice.models.Notification;
import com.example.notificationservice.models.NotificationType;
import com.example.notificationservice.repositories.NotificationRepository;
import com.example.notificationservice.repositories.NotificationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImp implements NotificationService {

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private NotificationRepository notificationRepository;

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private NotificationTypeRepository notificationTypeRepository;

    @Override
    public Notification Create(Notification notification) {
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public Iterable<Notification> List() {
        Iterable<Notification> notificationList = notificationRepository.findAll();
        return notificationList;
    }

    @Override
    public  Optional<Notification> Details(Integer id) {
        Optional<Notification> notification = notificationRepository.findById(id);

        return notification;
    }

    @Override
    public boolean Delete(Integer id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if(!notification.isPresent())
            return false;

        notificationRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean Update(Integer id, Notification notification) {
        Notification newNotification = notificationRepository.findById(id).get();
        if (newNotification == null) return false;

        newNotification.setContent(notification.getContent());
        newNotification.setLikedComment(notification.getLikedComment());
        newNotification.setOpen(notification.getOpen());
        newNotification.setPinnedPost(notification.getPinnedPost());
        newNotification.setSharedCollection(notification.getSharedCollection());
        newNotification.setUserId(notification.getUserId());
        newNotification.setActionUserId(notification.getActionUserId());
        newNotification.setNotificationType(notification.getNotificationType());

        notificationRepository.save(newNotification);
        return true;
    }

    @Override
    public  NotificationType CreateNotificationType(NotificationType notificationType) {
        notificationTypeRepository.save(notificationType);
        return notificationType;
    }

    @Override
    public Iterable<NotificationType> ListNotificationTypes() {
        Iterable<NotificationType> notificationTypeList = notificationTypeRepository.findAll();
        return notificationTypeList;
    }
}
