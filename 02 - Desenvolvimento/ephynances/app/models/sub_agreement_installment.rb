class SubAgreementInstallment < ActiveRecord::Base

  validates_presence_of :value, :status

  belongs_to :agreement_installment
  belongs_to :payment
end
