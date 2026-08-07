package valentinaferro.socialnetwork.runner;

import valentinaferro.socialnetwork.entity.Comment;
import valentinaferro.socialnetwork.entity.Post;
import valentinaferro.socialnetwork.entity.User;
import valentinaferro.socialnetwork.repository.CommentRepository;
import valentinaferro.socialnetwork.repository.PostRepository;
import valentinaferro.socialnetwork.repository.UserRepository;
import valentinaferro.socialnetwork.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor // costruttore con injection di repository e service
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeService postLikeService;

    @Override
    public void run(String... args) {

        // creo alcuni utenti di prova e li salvo nel database
        User valentina = userRepository.save(new User("valentina", "Valentina Ferro", "valentina@mail.it"));
        User marco = userRepository.save(new User("marco", "Marco Rossi", "marco@mail.it"));
        User giulia = userRepository.save(new User("giulia", "Giulia Bianchi", "giulia@mail.it"));

        // creo un paio di post, ognuno con un autore
        Post post1 = new Post("Il mio primo post!", LocalDate.now());
        post1.setAuthor(valentina);
        post1 = postRepository.save(post1);

        Post post2 = new Post("Oggi ho imparato Spring Boot", LocalDate.now());
        post2.setAuthor(marco);
        post2 = postRepository.save(post2);

        // aggiungo un commento a un post
        Comment comment1 = new Comment("Complimenti!", LocalDate.now());
        comment1.setUser(giulia);
        comment1.setPost(post1);
        commentRepository.save(comment1);

        // metto like a post1 da parte di marco e giulia: operazioni valide, nessun duplicato
        postLikeService.addLike(marco, post1);
        postLikeService.addLike(giulia, post1);
        System.out.println("Like aggiunti correttamente a post1.");

        // provo a far mettere like a marco una seconda volta sullo stesso post:
        // qui il Service deve bloccare l'operazione, verifichiamo che il vincolo funzioni
        try {
            postLikeService.addLike(marco, post1);
            System.out.println("ERRORE: il like duplicato è stato salvato, il vincolo non funziona!");
        } catch (IllegalStateException e) {
            System.out.println("Vincolo rispettato: " + e.getMessage());
        }
    }
}
