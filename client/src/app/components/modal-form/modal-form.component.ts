import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-modal-form',
  templateUrl: './modal-form.component.html',
  styleUrls: ['./modal-form.component.scss']
})
export class ModalFormComponent implements OnInit {
  @Input('type') type: string;
  @Input('name') name: string;
  @Input('description') description: string;
  @Input('idModal') idModal: number;
  @Output('validation') validation: EventEmitter<{ id: number, name: string, description: string }> = new EventEmitter<{id: number, name: string, description: string}>();


  constructor() {}

  ngOnInit() {
  }

  valider() {
    this.validation.emit({
      id: this.idModal,
      name: this.name,
      description: this.description
    });
  }
}
