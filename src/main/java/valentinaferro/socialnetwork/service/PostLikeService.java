package valentinaferro.socialnetwork.service;

import valentinaferro.socialnetwork.entity.Post;
import valentinaferro.socialnetwork.entity.PostLike;
import valentinaferro.socialnetwork.entity.User;
import valentinaferro.socialnetwork.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // genera il costruttore che inietta postLikeRepository
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public PostLike addLike(User user, Post post) {
        // prendo tutti i like già esistenti per questo post
        List<PostLike> existingLikes = postLikeRepository.findByPost(post);

        // controllo con lo Stream se tra questi c'è già un like dello stesso utente
        boolean alreadyLiked = existingLikes.stream()
                .anyMatch(like -> like.getUser().getId().equals(user.getId()));

        // se il like esiste già, blocco l'operazione invece di salvare un duplicato
        if (alreadyLiked) {
            throw new IllegalStateException("L'utente ha già messo like a questo post");
        }

        // nessun duplicato: creo e salvo il nuovo like
        return postLikeRepository.save(new PostLike(user, post));
    }
}
