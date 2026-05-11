import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap, tap, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { VeiculoLookupService } from '../../services/veiculo-lookup.service';
import { VeiculoSearchResult } from '../../models/veiculo-search-result.model';

@Component({
  selector: 'app-vehicle-plate-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vehicle-plate-search.html'
})
export class VehiclePlateSearch implements OnInit {
  private lookupService = inject(VeiculoLookupService);
  
  @Output() vehicleSelected = new EventEmitter<string>();

  searchControl = new FormControl('');
  
  results = signal<VeiculoSearchResult[]>([]);
  loading = signal(false);
  showDropdown = signal(false);
  hasError = signal(false);

  ngOnInit() {
    this.searchControl.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(val => {
        if (!val || val.trim().length < 3) {
          this.showDropdown.set(false);
          this.results.set([]);
          this.loading.set(false);
        } else {
          this.loading.set(true);
          this.showDropdown.set(true);
          this.hasError.set(false);
        }
      }),
      filter(val => !!val && val.trim().length >= 3),
      switchMap(val => {
        const cleanPlaca = val!.trim();
        return this.lookupService.searchByPlaca(cleanPlaca).pipe(
          catchError(() => {
            this.hasError.set(true);
            return of({ content: [] });
          })
        );
      })
    ).subscribe((response: any) => {
      this.results.set(response?.content || []);
      this.loading.set(false);
    });
  }

  selectVehicle(placa: string) {
    this.showDropdown.set(false);
    this.searchControl.setValue('', { emitEvent: false });
    this.vehicleSelected.emit(placa);
  }
}
