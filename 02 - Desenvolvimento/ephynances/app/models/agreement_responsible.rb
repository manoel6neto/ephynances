class AgreementResponsible < ActiveRecord::Base

  validates_presence_of :name, :email, :cpf

  has_many :agreements
end
