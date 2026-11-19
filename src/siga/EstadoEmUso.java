package siga;

public class EstadoEmUso implements EstadoReserva {

    @Override
    public String getNome() {
        return "EM_USO";
    }

    @Override
    public void retirar(Reserva reserva) {
        throw new IllegalStateException("Nao e possivel retirar em EM_USO.");
    }

    @Override
    public void devolver(Reserva reserva) {
        reserva.setEstado(new EstadoEncerrada());
    }

    @Override
    public void cancelar(Reserva reserva) {
        throw new IllegalStateException("Nao e possivel cancelar em EM_USO.");
    }
}