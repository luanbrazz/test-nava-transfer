import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TransferService } from 'src/app/service/transfer.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  transferForm!: FormGroup;
  transfers: any[] = [];
  minDate: string = '';

  constructor(private fb: FormBuilder, private transferService: TransferService) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadTransfers();
    this.setMinDate();
  }

  setMinDate(): void {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    this.minDate = today.toISOString().split('T')[0];
  }

  initializeForm(): void {
    this.transferForm = this.fb.group({
      sourceAccount: ['', Validators.required],
      destinationAccount: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(1)]],
      transferDate: ['', Validators.required],
    });
  }

  loadTransfers(): void {
    this.transferService.getAllTransfers().subscribe(
      (response) => {
        this.transfers = response;
      },
      (error) => {
        console.error('Erro ao carregar transferências:', error);
      }
    );
  }

  createTransfer(): void {
    if (this.transferForm.valid) {
      this.transferService.createTransfer(this.transferForm.value).subscribe(
        () => {
          alert('Transferência criada com sucesso!');
          this.transferForm.reset();
          this.loadTransfers();
        },
        (error) => {
          console.error('Erro ao criar transferência:', error);
          alert('Erro ao criar transferência.');
        }
      );
    } else {
      alert('Por favor, preencha todos os campos do formulário.');
    }
  }

  deleteTransfer(id: number, status: string): void {
    if (status === 'CANCELLED' || status === 'COMPLETED') {
      alert('Não é possível deletar transferências CANCELLED ou COMPLETED.');
      return;
    }

    if (confirm('Tem certeza que deseja deletar esta transferência?')) {
      this.transferService.cancelTransferById(id).subscribe(
        () => {
          alert('Transferência cancelada com sucesso!');
          this.loadTransfers();
        },
        (error) => {
          console.error('Erro ao cancelar transferência:', error);
          alert('Erro ao cancelar transferência.');
        }
      );
    }
  }

  viewDetails(transfer: any): void {
    alert(`Detalhes da transferência:\n
      ID: ${transfer.id}
      Conta Origem: ${transfer.sourceAccount}
      Conta Destino: ${transfer.destinationAccount}
      Valor: ${transfer.amount}
      Taxa: ${transfer.fee}
      Data de Transferência: ${transfer.transferDate}
      Data de Agendamento: ${transfer.scheduleDate}
      Status: ${transfer.status}`);
  }
}
