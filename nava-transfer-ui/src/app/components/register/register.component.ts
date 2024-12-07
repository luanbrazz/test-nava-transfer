import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { JwtService } from 'src/app/service/jwt.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor(
    private service: JwtService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group(
      {
        name: ['', [Validators.required]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required]],
        confirmPassword: ['', [Validators.required]],
      },
      { validator: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      formGroup.get('confirmPassword')?.setErrors({ passwordMismatch: true });
    } else {
      formGroup.get('confirmPassword')?.setErrors(null);
    }
  }

  submitForm() {
    if (this.registerForm.valid) {
      this.service.register(this.registerForm.value).subscribe(
        (response) => {
          if (response && response.id) {
            alert('Olá ' + response.name + ' você foi cadastrado com sucesso!');
            this.router.navigate(['/login']);
          } else {
            alert('Erro inesperado: Resposta inválida do servidor.');
          }
        },
        (error) => {
          console.error('Erro ao registrar:', error);
          if (error.status === 400) {
            alert('Erro: Já existe um usuário com este email.');
          } else if (error.status === 500) {
            alert('Erro no servidor. Tente novamente mais tarde.');
          } else {
            alert('Erro ao registrar.');
          }
        }
      );
    } else {
      alert('Por favor, preencha o formulário corretamente.');
    }
  }
}
