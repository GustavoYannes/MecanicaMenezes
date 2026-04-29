import { Component, EventEmitter, OnInit, Output, OnDestroy } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-cliente-search',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './cliente-search.html',
  styles: ``
})
export class ClienteSearch implements OnInit, OnDestroy {
  searchControl = new FormControl('');
  @Output() search = new EventEmitter<string>();
  private destroy$ = new Subject<void>();

  ngOnInit() {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe(value => {
        this.search.emit(value || '');
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  clearSearch() {
    this.searchControl.setValue('');
  }
}
