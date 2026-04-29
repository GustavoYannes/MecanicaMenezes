import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cliente-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cliente-pagination.html',
  styles: ``
})
export class ClientePagination {
  @Input() currentPage: number = 0;
  @Input() totalPages: number = 0;
  @Input() totalElements: number = 0;
  @Input() first: boolean = true;
  @Input() last: boolean = true;
  @Output() pageChange = new EventEmitter<number>();

  get pages(): number[] {
    // Retorna uma lista de páginas para exibir.
    // Ex: Se estamos na página 2, pode exibir 1, 2, 3...
    // Como a API é 0-indexed, somamos 1 para a exibição na interface.
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages - 1, start + 4);
    
    const displayPages = [];
    for (let i = start; i <= end; i++) {
      displayPages.push(i);
    }
    return displayPages;
  }

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages && page !== this.currentPage) {
      this.pageChange.emit(page);
    }
  }

  previous() {
    if (!this.first) {
      this.goToPage(this.currentPage - 1);
    }
  }

  next() {
    if (!this.last) {
      this.goToPage(this.currentPage + 1);
    }
  }
}
