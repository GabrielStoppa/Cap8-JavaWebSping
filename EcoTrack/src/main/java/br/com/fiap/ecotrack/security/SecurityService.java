package br.com.fiap.ecotrack.security;

import br.com.fiap.ecotrack.model.Notificacao;
import br.com.fiap.ecotrack.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    /**
     * Verifica se o usuário atual é o usuário com o ID especificado
     * @param userId ID do usuário a verificar
     * @return true se o usuário autenticado é o usuário com o ID especificado
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        // Se o usuário for ADMIN, permitir acesso
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        // Verificar se o email do usuário autenticado corresponde ao ID fornecido
        return authentication.getPrincipal() != null &&
                userId != null &&
                currentUserEmail.equals(userId.toString());
    }
    
    /**
     * Verifica se a notificação pertence ao usuário atual
     * @param notificacaoId ID da notificação a verificar
     * @return true se a notificação pertence ao usuário autenticado
     */
    public boolean isNotificacaoUsuario(Long notificacaoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        // Se o usuário for ADMIN, permitir acesso
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        
        // Verificar se a notificação pertence ao usuário autenticado
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId).orElse(null);
        if (notificacao == null) {
            return false;
        }
        
        return notificacao.getUsuario() != null && 
               notificacao.getUsuario().getEmail().equals(currentUserEmail);
    }
}
