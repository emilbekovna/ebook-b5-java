package kg.eBook.ebookb5.services;

import kg.eBook.ebookb5.dto.requests.MailNewBookRequest;
import kg.eBook.ebookb5.dto.responses.NotificationFindByIdResponse;
import kg.eBook.ebookb5.dto.responses.NotificationResponse;
import kg.eBook.ebookb5.dto.responses.SimpleResponse;
import kg.eBook.ebookb5.enums.BookStatus;
import kg.eBook.ebookb5.exceptions.NotFoundException;
import kg.eBook.ebookb5.models.Book;
import kg.eBook.ebookb5.models.Notification;
import kg.eBook.ebookb5.models.User;
import kg.eBook.ebookb5.repositories.BookRepository;
import kg.eBook.ebookb5.repositories.NotificationRepository;
import kg.eBook.ebookb5.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static kg.eBook.ebookb5.dto.responses.NotificationResponse.view;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final MailingService mailingService;

    public SimpleResponse acceptedBook(Long bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Книга не найдено"));

        Notification notification = new Notification();

        book.setBookStatus(BookStatus.ACCEPTED);
        book.setPublishedDate(LocalDate.now());
        bookRepository.save(book);

        notification.setBookStatus(BookStatus.ACCEPTED);
        notification.setVendor(book.getOwner());
        notification.setCreatedAt(LocalDate.now());
        notification.setBookId(book.getId());

        notificationRepository.save(notification);

        MailNewBookRequest mailNewBookRequest = new MailNewBookRequest(
                book.getMainImage(),
                book.getName(),
                book.getPrice()
        );

        if(book.getBookStatus().equals(BookStatus.ACCEPTED)) {
            mailingService.sendNewBookMessage(mailNewBookRequest);
        }

        return new SimpleResponse(book.getName() + " был успешно принят!");
    }

    public SimpleResponse rejectedBook(Long bookId, String description) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("Книга не найдено"));

        Notification notification = new Notification();

        book.setBookStatus(BookStatus.REJECTED);
        book.setPublishedDate(LocalDate.now());
        bookRepository.save(book);

        notification.setBookStatus(BookStatus.REJECTED);
        notification.setVendor(book.getOwner());
        notification.setCreatedAt(LocalDate.now());
        notification.setDescription(description);
        notification.setBookId(book.getId());

        notificationRepository.save(notification);

        return new SimpleResponse(book.getName() + " был отклонён!");
    }

    public List<NotificationResponse> findAllNotificationsByVendor(Authentication authentication) {
        User vendor = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<NotificationResponse> findAllNotitcation = notificationRepository.findAllNotifications(vendor.getId());
        return findAllNotitcation;
    }

    public NotificationFindByIdResponse findByNotificationId(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Уведомление не найдено"));

        notification.setRead(true);
        notificationRepository.save(notification);

        return new NotificationFindByIdResponse(notification);
    }

    public List<NotificationResponse> markAsRead(Authentication authentication) {
        User vendor = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        for (Notification notification : vendor.getNotifications()) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(vendor.getNotifications());
        return view(vendor.getNotifications());
    }
}
