import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { JwtService } from 'src/app/service/jwt.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(
    private service: JwtService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  submitForm() {
    if (this.loginForm.valid) {
      this.service.login(this.loginForm.value).subscribe(
        (response) => {
          console.log(response);
          if (response && response.jwtToken) {
            const jwtToken = response.jwtToken;
            localStorage.setItem('jwt', jwtToken);
            this.router.navigate(['/dashboard']);
          } else {
            alert('Falha no login. Token inválido.');
          }
        },
        (error) => {
          console.error('Erro no login:', error);
          alert('Erro ao realizar login. Verifique suas credenciais.');
        }
      );
    } else {
      alert('Por favor, preencha o formulário corretamente.');
    }
  }
}
