package siga;

import java.util.List;

/**
 * PROBLEMA 1 (parte B) - a MESMA conversao, escrita de novo, em outro lugar.
 *
 * Aqui alguem percebeu que a situacao as vezes vem em minuscula e corrigiu.
 * A correcao nao chegou ao CatalogoEquipamentos: as duas versoes ja divergem, e o
 * mesmo Equipamento aparece indisponivel em um lugar e disponivel no outro.
 */
public class RelatorioAcervo {

    private final FonteEquipamentos fonte;

    public RelatorioAcervo(FonteEquipamentos fonte) {
        this.fonte = fonte;
    }

    public List<Equipamento> listarTodos() {
        return fonte.listarEquipamentos();
    }

    public void imprimir() {
        System.out.println("   Situacao segundo o relatorio:");
        for (Equipamento item : listarTodos()) {
            System.out.println("     " + item + " - "
                    + (item.isDisponivel() ? "disponivel" : "baixado"));
        }
    }
}
