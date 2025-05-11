package br.com.fiap.ecotrack.controller;

import br.com.fiap.ecotrack.dto.AuthRequestDTO;
import br.com.fiap.ecotrack.dto.AuthResponseDTO;
import br.com.fiap.ecotrack.dto.UsuarioDTO;
import br.com.fiap.ecotrack.model.Usuario;
import br.com.fiap.ecotrack.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza a autenticação do usuário e retorna um token JWT")
    public ResponseEntity<AuthResponseDTO> autenticar(@Valid @RequestBody AuthRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.autenticar(loginRequest));
    }

    @PostMapping("/registro/cidadao")
    @Operation(summary = "Registrar cidadão", description = "Registra um novo usuário com o papel de CIDADAO")
    public ResponseEntity<UsuarioDTO> registrarCidadao(@Valid @RequestBody UsuarioDTO registroRequest) {
        return ResponseEntity.ok(authService.registrar(registroRequest, Usuario.TipoUsuario.CIDADAO));
    }

    @PostMapping("/registro/empresa")
    @Operation(summary = "Registrar empresa", description = "Registra um novo usuário com o papel de EMPRESA")
    public ResponseEntity<UsuarioDTO> registrarEmpresa(@Valid @RequestBody UsuarioDTO registroRequest) {
        return ResponseEntity.ok(authService.registrar(registroRequest, Usuario.TipoUsuario.EMPRESA));
    }
}
