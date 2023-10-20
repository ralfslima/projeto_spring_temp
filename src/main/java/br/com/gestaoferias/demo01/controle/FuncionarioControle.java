package br.com.gestaoferias.demo01.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gestaoferias.demo01.entities.Ferias;
import br.com.gestaoferias.demo01.entities.Funcionario;
import br.com.gestaoferias.demo01.servico.FeriasServico;
import br.com.gestaoferias.demo01.servico.FuncionarioServico;


@RestController
@RequestMapping("/funcs")
public class FuncionarioControle {
    
    @Autowired
    private FuncionarioServico fs;

    @Autowired
    private FeriasServico fs2;

    @DeleteMapping("/remover/{matricula}/{idFerias}")
    public ResponseEntity<Funcionario> remover(@PathVariable int matricula, @PathVariable long idFerias) {
        // Obter todos os dados do funcionário, através da matrícula
        Funcionario f = fs.findById(matricula);

        // Criar uma cópia do funcionário, exceto pela característica de férias
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setMatricula(f.getMatricula());
        novoFuncionario.setNome(f.getNome());
        novoFuncionario.setFuncsDepto(f.getFuncsDepto());
        novoFuncionario.setFuncsFerias(null);

        // Alterando os dados do funcionário (primeiro preciso desvincular o uso das férias)
        ResponseEntity<Funcionario> retorno = fs.cadastrarAlterar(novoFuncionario, "alterar");
    
        // Removendo as férias da tabela de férias (agora que não há mais vínculo, consigo remover as férias)
        fs2.remover(idFerias);

        // Exibir o retorno
        return retorno;
    }

    @PutMapping("/alterar")
    public ResponseEntity<Funcionario> alterar(@RequestBody Funcionario fc) {
        return fs.cadastrarAlterar(fc, "alterar");
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Funcionario> cadastrar(@RequestBody Funcionario fc) {
        return fs.cadastrarAlterar(fc, "cadastrar");
    }

    @GetMapping(value = "/listar/{matricula}")
	public ResponseEntity<Funcionario> findById(@PathVariable("matricula") int matricula) {
		Funcionario funcionario = fs.findById(matricula);
		return ResponseEntity.ok().body(funcionario);
	}

    @GetMapping("/listar")
    public Iterable<Funcionario> listar() {
        return fs.listar();
    }

    
}
