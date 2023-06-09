//------------------Package----------------------
package br.com.uniamerica.gpc.GPCbackend.entity;

//------------------Imports----------------------

import br.com.uniamerica.gpc.GPCbackend.annotations.UniqueValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDateTime;

//------------------------------------------------
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Audited
@AuditTable(value = "ativos_audit",schema = "audit")
@Table(name = "ativos", schema = "public")
public class Ativo extends AbstractEntity {
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "O objeto categoria não foi informado.")
    private Categoria categoria;

    @Getter
    @Setter
    @Column(name = "cod_patrimonio", nullable = false, unique = true, length = 15)
    @NotNull(message = "O ID do patrimônio do ativo não pode ser nulo.")
    @NotBlank(message = "O ID da patrimônio do ativo não pode ser vazio.")
    @Size(max = 15,message = "A ID Patrimonio do ativo deve ter no máximo 15 caracteres.")
    @UniqueValue(message = "ID patrimônio está registrado no banco de dados.")
    private String idPatrimonio;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "condicao_patrimonio", nullable = false)
    @NotNull(message = "A condição do ativo não pode ser nula")
    private Condicao condicao;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status_disponibilidade", nullable = false)
    @NotNull(message = "O status de disponibilidade do ativo não pode ser nulo")
    private Status status;

    @Getter
    @Setter
    @Column(name = "dt_entrada", nullable = false)
    private LocalDateTime dataEntrada;

    @PrePersist
    private void prePersist() {
        this.dataEntrada = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        this.dataEntrada = LocalDateTime.now();
    }

}
