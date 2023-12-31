
import { Credential } from "./credential";
import { UserImage } from "./user-image";

export class Customer {
  
  constructor(
    public id: number,
    public identifier: string,
    public ssn: string | null | undefined,
    public firstname: string,
    public lastname: string,
    public isMale: boolean,
    public email: string,
    public phone: string,
    public birthdate: Date | string,
    public facebookUrl: string | null,
    public instagramUrl: string | null,
    public linkedinUrl: string | null,
    public userImage: UserImage | null,
    public credential: Credential) {
  }
  
  
  
}











