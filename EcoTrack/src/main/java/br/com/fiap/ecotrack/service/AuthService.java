package br.com.fiap.ecotrack.service;

import br.com.fiap.ecotrack.dto.AuthRequestDTO;
import br.com.fiap.ecotrack.dto.AuthResponseDTO;
import br.com.fiap.ecotrack.dto.UsuarioDTO;
import br.com.fiap.ecotrack.exception.AppException;
import br.com.fiap.ecotrack.model.Usuario;
import br.com.fiap.ecotrack.repository.UsuarioRepository;
import br.com.fiap.ecotrack.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponseDTO autenticar(AuthRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getSenha()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new AppException("Usuário não encontrado", HttpStatus.NOT_FOUND));

            String jwt = tokenProvider.generateToken(usuario);

            return AuthResponseDTO.builder()
                    .token(jwt)
                    .tipo("Bearer")
                    .usuarioId(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .tipoUsuario(usuario.getTipo().name())
                    .build();
        } catch (AuthenticationException e) {
            throw new AppException("Email ou senha inválidos", HttpStatus.UNAUTHORIZED);
        }
    }

    public UsuarioDTO registrar(UsuarioDTO registroRequest, Usuario.TipoUsuario tipoUsuario) {
        if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
            throw new AppException("Email já cadastrado", HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(registroRequest.getNome());
        usuario.setEmail(registroRequest.getEmail());
        usuario.setSenha(passwordEncoder.encode(registroRequest.getEmail())); // Temporário, será alterado após o primeiro login
        usuario.setTelefone(registroRequest.getTelefone());
        usuario.setTipo(tipoUsuario);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return UsuarioDTO.fromEntity(usuarioSalvo);
    }
}
