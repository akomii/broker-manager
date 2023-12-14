import moment, { Duration } from 'moment';

export class MomentWrapper {
  static formatDateToGermanLocale(date: Date): string {
    return moment(date).locale("de").format("DD.MM.YYYY HH:mm");
  }
}

export type MomentDuration = Duration;

export default MomentWrapper;
