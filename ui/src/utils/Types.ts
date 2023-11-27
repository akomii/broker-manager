export interface Principal {
  [key: string]: string;
  name: string;
  organization: string;
  email: string;
  phone: string;
}

export interface Organization {
  id: number,
  name: string;
}