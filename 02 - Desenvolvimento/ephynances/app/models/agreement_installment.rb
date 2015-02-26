class AgreementInstallment < ActiveRecord::Base

  validates_presence_of :value, :status, :dueDate

  belongs_to :agreement
  has_many :sub_agreement_installments
  belongs_to :payment
end
