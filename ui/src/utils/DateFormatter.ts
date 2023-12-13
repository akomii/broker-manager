import Locale from "@/locales/de.json";

export class DateFormatter {
  private static instance: DateFormatter;
  private locale: string;
  private dateOptions: Intl.DateTimeFormatOptions;
  private timeOptions: Intl.DateTimeFormatOptions;
  private formatters: { [key: string]: Intl.DateTimeFormat };

  private constructor() {
    this.locale = "de-DE";
    this.dateOptions = this.createDateFormatOptions(Locale.dateFormat);
    this.timeOptions = {
      hour: "2-digit",
      minute: "2-digit",
      hour12: false,
    };
    this.formatters = {};
  }

  public static getInstance(): DateFormatter {
    if (!this.instance) {
      this.instance = new DateFormatter();
    }
    return this.instance;
  }

  private createDateFormatOptions(
    formatString: string
  ): Intl.DateTimeFormatOptions {
    const formatMapping: { [key: string]: Intl.DateTimeFormatOptions } = {
      d: { day: "numeric" },
      dd: { day: "2-digit" },
      m: { month: "numeric" },
      mm: { month: "2-digit" },
      y: { year: "2-digit" },
      yy: { year: "numeric" },
    };
    let options: Intl.DateTimeFormatOptions = {};
    Object.keys(formatMapping).forEach((key) => {
      if (formatString.includes(key)) {
        options = { ...options, ...formatMapping[key] };
      }
    });
    return options;
  }

  private getFormatter(
    options: Intl.DateTimeFormatOptions
  ): Intl.DateTimeFormat {
    const key = `${this.locale}-${JSON.stringify(options)}`;
    if (!this.formatters[key]) {
      this.formatters[key] = new Intl.DateTimeFormat(this.locale, options);
    }
    return this.formatters[key];
  }

  public formatToLocalDate(date: Date): string {
    if (!(date instanceof Date) || isNaN(date.getTime())) {
      return "";
    }
    const formattedDate = this.getFormatter(this.dateOptions).format(date);
    const formattedTime = this.getFormatter(this.timeOptions).format(date);
    return `${formattedDate} ${formattedTime}`;
  }
}
