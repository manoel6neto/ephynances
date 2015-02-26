# encoding: UTF-8
class CityOrgansController < ApplicationController
  before_action :set_city_organ, only: [:show, :edit, :update, :destroy]

  # GET /city_organs
  # GET /city_organs.json
  def index
    @city_organs = CityOrgan.all
  end

  # GET /city_organs/1
  # GET /city_organs/1.json
  def show
  end

  # GET /city_organs/new
  def new
    @city_organ = CityOrgan.new
  end

  # GET /city_organs/1/edit
  def edit
  end

  # POST /city_organs
  # POST /city_organs.json
  def create
    @city_organ = CityOrgan.new(city_organ_params)

    respond_to do |format|
      if @city_organ.save
        format.html { redirect_to @city_organ, notice: 'City organ was successfully created.' }
        format.json { render :show, status: :created, location: @city_organ }
      else
        format.html { render :new }
        format.json { render json: @city_organ.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /city_organs/1
  # PATCH/PUT /city_organs/1.json
  def update
    respond_to do |format|
      if @city_organ.update(city_organ_params)
        format.html { redirect_to @city_organ, notice: 'City organ was successfully updated.' }
        format.json { render :show, status: :ok, location: @city_organ }
      else
        format.html { render :edit }
        format.json { render json: @city_organ.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /city_organs/1
  # DELETE /city_organs/1.json
  def destroy
    @city_organ.destroy
    respond_to do |format|
      format.html { redirect_to city_organs_url, notice: 'City organ was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_city_organ
      @city_organ = CityOrgan.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def city_organ_params
      params.require(:city_organ).permit(:cnpj, :cityName, :stateName, :regionName, :organName)
    end
end
