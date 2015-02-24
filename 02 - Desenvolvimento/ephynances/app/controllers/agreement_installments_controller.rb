class AgreementInstallmentsController < ApplicationController
  before_action :set_agreement_installment, only: [:show, :edit, :update, :destroy]

  # GET /agreement_installments
  # GET /agreement_installments.json
  def index
    @agreement_installments = AgreementInstallment.all
  end

  # GET /agreement_installments/1
  # GET /agreement_installments/1.json
  def show
  end

  # GET /agreement_installments/new
  def new
    @agreement_installment = AgreementInstallment.new
  end

  # GET /agreement_installments/1/edit
  def edit
  end

  # POST /agreement_installments
  # POST /agreement_installments.json
  def create
    @agreement_installment = AgreementInstallment.new(agreement_installment_params)

    respond_to do |format|
      if @agreement_installment.save
        format.html { redirect_to @agreement_installment, notice: 'Agreement installment was successfully created.' }
        format.json { render :show, status: :created, location: @agreement_installment }
      else
        format.html { render :new }
        format.json { render json: @agreement_installment.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /agreement_installments/1
  # PATCH/PUT /agreement_installments/1.json
  def update
    respond_to do |format|
      if @agreement_installment.update(agreement_installment_params)
        format.html { redirect_to @agreement_installment, notice: 'Agreement installment was successfully updated.' }
        format.json { render :show, status: :ok, location: @agreement_installment }
      else
        format.html { render :edit }
        format.json { render json: @agreement_installment.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /agreement_installments/1
  # DELETE /agreement_installments/1.json
  def destroy
    @agreement_installment.destroy
    respond_to do |format|
      format.html { redirect_to agreement_installments_url, notice: 'Agreement installment was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_agreement_installment
      @agreement_installment = AgreementInstallment.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def agreement_installment_params
      params.require(:agreement_installment).permit(:value, :status, :paymentDate, :dueDate, :confirmationDate)
    end
end
