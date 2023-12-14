import moment from "moment";

export class MomentWrapper {
  static formatDateToGermanLocale(date: Date): string {
    return moment(date).locale("de").format("DD.MM.YYYY HH:mm");
  }
}

export default MomentWrapper;
