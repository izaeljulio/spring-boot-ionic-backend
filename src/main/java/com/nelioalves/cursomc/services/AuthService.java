package com.nelioalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassord();
		cliente.setSenha(pe.encode(newPass));
		
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassord() {
		char[] vet = new char[10];
		for (int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);
		if (opt ==0) { //gera um digito
			return (char) (rand.nextInt(10) + 48); //tabela de caracteres dígitos de 48 a 58  
		}else if (opt == 1) { //gera letra maiúscula
			return (char) (rand.nextInt(26) + 65);//tabela de caracteres letras maíusculas inicia em 65 
		}else { // gera letra minúscula
			return (char) (rand.nextInt(26) + 97);//minúscula começa em 97
		}
	}
}
