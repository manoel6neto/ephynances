class AgreementResponsible < ActiveRecord::Base

  validates_presence_of :name, :email, :cpf
end
